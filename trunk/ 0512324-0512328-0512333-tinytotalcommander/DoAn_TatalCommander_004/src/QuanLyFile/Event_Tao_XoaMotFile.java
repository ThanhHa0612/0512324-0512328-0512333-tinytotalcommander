/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package QuanLyFile;

import java.util.EventObject;

/**
 * Sự kiện khi tạo mới hoặc xóa một file
 * phát đi để bạn hiện thị file có thể cập nhật kiệp thời
 * @author Administrator
 */
public class Event_Tao_XoaMotFile extends EventObject {
        public Event_Tao_XoaMotFile(Object source) {
            super(source);
        }
    }

