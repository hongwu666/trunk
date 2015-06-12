import java.io.File;

import com.lodogame.game.utils.json.Json;
import com.lodogame.model.DiskInfo;

/**
 * 服务器状态监控
 * 
 * @author CJ
 * 
 */
public class HardWardMonitor {

	public static DiskInfo getDiskInfo() {
		File file = new File("/data");
		int totalSpace = (int) (file.getTotalSpace() / 1024 / 1024 / 1024);
		int usableSpace = (int) (file.getUsableSpace() / 1024 / 1024 / 1024);

		DiskInfo diskInfo = new DiskInfo(totalSpace, usableSpace);

		return diskInfo;

	}

	public static void main(String[] args) {
		DiskInfo diskInfo = getDiskInfo();
		System.out.println(Json.toJson(diskInfo));

	}

}
