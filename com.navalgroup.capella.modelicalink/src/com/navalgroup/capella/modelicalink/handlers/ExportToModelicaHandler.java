package com.navalgroup.capella.modelicalink.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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

	private final String destPath = "C:\\Users\\Ludo\\Documents\\Development\\capella\\modelica";

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
