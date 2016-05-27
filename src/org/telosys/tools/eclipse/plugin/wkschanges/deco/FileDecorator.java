package org.telosys.tools.eclipse.plugin.wkschanges.deco;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

//@SuppressWarnings("restriction")
public class FileDecorator extends LabelProvider implements ILightweightLabelDecorator  {

//	
//	public Image decorateImage(Image image, Object element) {
//		/*
//		 * image   : the input image to decorate, or null if the element has no image
//		 *           Here never null ( 1rts time contains the standard "folder" image
//		 * element : the element whose image is being decorated
//		 */
//		//Image telosysFolderImage = PluginImages.getImage(PluginImages.TELOSYS_FOLDER);
//		if (element instanceof Folder) {
//
//			Folder folder = (Folder) element;
//
//			// Apply decorator for "TelosysTools" (ignore case) or any "telosys*tools"
//			final String folderNameLowerCase = folder.getName().toLowerCase() ;
//			if ( "telosystools".equalsIgnoreCase( folderNameLowerCase )  ) {
//				return getTelosysToolsFolderImage() ;
//			}
//			else {
//				if ( folderNameLowerCase.startsWith("telosys") 
//						&& folderNameLowerCase.endsWith("tools") ) {
//					return getTelosysToolsFolderImage() ;
//				}
//			}
//		}
//		return null; // null means "no decoration is to be applied"
//	}
//	
//	public Image getTelosysToolsFolderImage() {
//		return PluginImages.getImage(PluginImages.TELOSYS_FOLDER);
//		
//	}
//	
//	public String decorateText(String text, Object element) {
//		// NOTHING TO DECORATE
//		return null;
//	}

	@Override
	public void decorate(Object object, IDecoration decoration) {
		PluginLogger.debug("decorate()... " );

		if ( object instanceof IResource ) {
			IResource resource = (IResource) object ;
			PluginLogger.debug("decorate() : resource = " + resource.getName() );
			if ( resource.exists() ) {
				IMarker errorMarker = FileMarker.findErrorMarker(resource);
				if ( errorMarker != null ) {
					// Error marker found => set the error decoration
					PluginLogger.debug("decorate() : error marker found" );
//					decoration.addOverlay(ImageDescriptor.createFromFile(FileDecorator.class, ICON), IDecoration.TOP_RIGHT);
					decoration.addPrefix("<!> ");
					decoration.addSuffix(" (error)");
//					decoration.setFont(font);
//					decoration.setForegroundColor(color);
				}
			}
			else {
				PluginLogger.debug("decorate() : the resource doesn't exist" );
			}
		}
	}
}
