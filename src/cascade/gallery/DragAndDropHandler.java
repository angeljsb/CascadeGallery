/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cascade.gallery;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Angel
 */
public class DragAndDropHandler extends DropTargetAdapter {
    
    private ImageFilesControl filesControl;
    
    public DragAndDropHandler(ImageFilesControl filesControl){
        this.filesControl = filesControl;
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        // Accept copy drops
        dtde.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = dtde.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors) {
            
            try {

                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {

                    // Get all of the dropped files
                    List<File> files = (List) transferable.getTransferData(flavor);
                    
                    if(files.isEmpty()){
                        break;
                    }
                    
                    if(files.get(0).isDirectory()){
                        this.filesControl.setImages(files.get(0));
                        break;
                    }else{
                        File[] arr = new File[0];
                        this.filesControl.setImages(files.toArray(arr));
                        break;
                    }

                }

            } catch (UnsupportedFlavorException | IOException e) {

                // Print out the error stack
                e.printStackTrace(System.err);

            }
        }

        // Inform that the drop is complete
        dtde.dropComplete(true);
        
    }
    
}
