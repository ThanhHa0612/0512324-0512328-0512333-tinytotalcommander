/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DuyetFile;

import QuanLyFile.BoQuanLyFile;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author Administrator
 */

// <editor-fold defaultstate="collapsed" desc="class DroppableTable">

/**
 * Lớp DroppableTable là một JTable có khả năng DnD
 * tham khảo http://www.codeproject.com/KB/list/dnd.aspx?display=Print
 * @author Administrator
 */
public class DroppableTable extends JTable
        implements DropTargetListener, DragSourceListener, DragGestureListener {

    BoQuanLyFile boQuanLyFile = new BoQuanLyFile();
    DropTarget dropTarget = new DropTarget(this, this);
    DragSource dragSource = DragSource.getDefaultDragSource();
    BangDuyetFile bangDuyetFile;

    public DroppableTable(TableModel model, BangDuyetFile bangDuyet) {
        bangDuyetFile = bangDuyet;
        dragSource.createDefaultDragGestureRecognizer(
                this, DnDConstants.ACTION_COPY, this);
        setModel(model);
    }
//Các hàm bắt buộc phải override
    public void dragDropEnd(DragSourceDropEvent DragSourceDropEvent) {
    }

    public void dragEnter(DragSourceDragEvent DragSourceDragEvent) {
    }

    public void dragExit(DragSourceEvent DragSourceEvent) {
    }

    public void dragOver(DragSourceDragEvent DragSourceDragEvent) {
    }

    public void dropActionChanged(DragSourceDragEvent DragSourceDragEvent) {
    }

    public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
        dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
    }

    public void dragExit(DropTargetEvent dropTargetEvent) {
    }

    public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
    }

    public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
    }

    /**
     * Xử lý xử kiện khi kéo thả file vào DTable
     * @param dropTargetDropEvent   tham số của xử kiện
     */
    public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
        try {
            Transferable tr = dropTargetDropEvent.getTransferable();
            //Nếu đối tượng phù hợp được drop
            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                //Đặt xử kiện là copy
                dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
                java.util.List fileList = null;
                try {
                    fileList = (java.util.List) tr.getTransferData(DataFlavor.javaFileListFlavor);
                } catch (IOException ex) {
                    Logger.getLogger(DroppableTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Tương đương foreach
                Iterator iterator = fileList.iterator();
                while (iterator.hasNext()) {
                    File file = (File) iterator.next();
                    boQuanLyFile.copyDirectory(file,
                            new File(bangDuyetFile.getTenFile() + "\\" + file.getName()), false);
                 //   JOptionPane.showMessageDialog(null, file.getAbsolutePath());
                //((DefaultListModel)getModel()).addElement(file.getPath());

                    bangDuyetFile.capNhatBangDuyetThuMuc(bangDuyetFile.getTenFile(), bangDuyetFile.getScrollPane_HienThiBang());
                }
                dropTargetDropEvent.getDropTargetContext().dropComplete(true);
            } else {
                System.err.println("Rejected");
                dropTargetDropEvent.rejectDrop();
            }
        } catch (UnsupportedFlavorException ex) {
            ex.printStackTrace();
            dropTargetDropEvent.rejectDrop();
            Logger.getLogger(DroppableTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException io) {
            io.printStackTrace();
            dropTargetDropEvent.rejectDrop();
        }
    }

    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
        if (getSelectedRow() == -1) {
            return;
        }
        int obj = getSelectedRow();
        if (obj < 0) {
            // Nothing selected, nothing to drag
            System.out.println("Nothing selected - beep");
            getToolkit().beep();
        } else {

            FileSelection transferable =
                    new FileSelection(new File(getValueAt(obj, 3).toString()));
            dragGestureEvent.startDrag(
                    DragSource.DefaultCopyDrop,
                    transferable,
                    this);
        }
    }
//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="Class FileSelection">

    /**
     * Tạo lớp lấy thông tin file được chọn để đưa ra cho WindowsExplorer
     * tham khảo http://www.codeproject.com/KB/list/dnd.aspx?display=Print
     */
    public class FileSelection extends Vector implements Transferable {

        final static int FILE = 0;
        final static int STRING = 1;
        final static int PLAIN = 2;
        DataFlavor flavors[] = {DataFlavor.javaFileListFlavor,
            DataFlavor.stringFlavor,
            DataFlavor.plainTextFlavor};

        public FileSelection(File file) {
            addElement(file);
        }
        /* Returns the array of flavors in which it can provide the data. */

        public synchronized DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }
        /* Returns whether the requested flavor is supported by this object. */
        /* Kiểm tra xem đối tượng được chọn có hợp lệ không */

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            boolean b = false;
            b |= flavor.equals(flavors[FILE]);
            b |= flavor.equals(flavors[STRING]);
            b |= flavor.equals(flavors[PLAIN]);
            return (b);
        }

        /**
         * If the data was requested in the "java.lang.String" flavor,
         * return the String representing the selection.
         * trả về đối tượng thích hợp nếu File, hay đường dẫn tuyệt đối của file
         */
        public synchronized Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException, IOException {
            if (flavor.equals(flavors[FILE])) {
                return this;
            } else if (flavor.equals(flavors[PLAIN])) {
                return new StringReader(((File) elementAt(0)).getAbsolutePath());
            } else if (flavor.equals(flavors[STRING])) {
                return ((File) elementAt(0)).getAbsolutePath();
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }
}
//</editor-fold>
