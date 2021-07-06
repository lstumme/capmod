package com.navalgroup.capella.modelicalink.handlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(
					new FileWriter("C:\\Users\\Ludo\\Documents\\Development\\capella\\workspace\\output.log"));
			writer.write(event.toString() + "\r\n");
			ISelection selection = HandlerUtil.getCurrentSelection(event);
			writer.write(selection.toString() + "\r\n");
			Collection<EObject> semanticElements = CapellaAdapterHelper
					.resolveSemanticObjects(((IStructuredSelection) selection).toList());
			writer.write(semanticElements.toString() + "\r\n");
			for (EObject object : semanticElements) {
				writer.write(object.toString() + "\r\n");
				if (object instanceof PhysicalComponent) {
					writer.write("Object is PhysicalComponent\r\n");
					try {
						ModelicaGenerator generator = new ModelicaGenerator(object, new File(destPath),
								new ArrayList<String>());
						generator.generate(new BasicMonitor());
					} catch (IOException e) {
						e.printStackTrace(new PrintWriter(writer));
					}
				}
			}
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

}
