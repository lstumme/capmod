package com.navalgroup.capella.modelicalink.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;

import com.navalgroup.capella.modelicalink.generators.ModelicaGenerator;

public class ExportToModelicaHandler extends AbstractHandler {

	private static final Logger LOG = Logger.getLogger(ExportToModelicaHandler.class.getName());

	private final String destPath = System.getProperty("user.home") + "\\tmp\\modelica4capella";

	public ExportToModelicaHandler() {
		super();

		File f1 = new File(destPath);

		if (f1.exists()) {
			LOG.info("Destination folder already exists. Skipping creation.");
			return;
		}

		boolean bool = f1.mkdir();
		if (bool) {
			LOG.info("Destination folder is created successfully");
		} else {
			LOG.severe("Destination folder can not be created!");
			if (!f1.canWrite()) {
				LOG.severe("Destination folder is not writable. Please check file system permissions.");
			}
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Collection<EObject> semanticElements = CapellaAdapterHelper
				.resolveSemanticObjects(((IStructuredSelection) selection).toList());
		for (EObject object : semanticElements) {
			if (object instanceof PhysicalComponent) {
				try {
					ModelicaGenerator generator = new ModelicaGenerator(object, new File(destPath),
							new ArrayList<String>());
					generator.generate(new BasicMonitor(), false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
