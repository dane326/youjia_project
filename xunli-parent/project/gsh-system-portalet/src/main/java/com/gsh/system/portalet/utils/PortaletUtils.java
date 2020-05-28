package com.gsh.system.portalet.utils;

import org.springframework.web.multipart.MultipartFile;

import com.gsh.common.utils.file.MinioClientUtils;

import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

public class PortaletUtils {

	public static PortaletUtils instance;

	public static PortaletUtils getInstance() throws InvalidPortException, InvalidEndpointException {
		if (null == instance) {
			instance = new PortaletUtils();
		}
		return instance;
	}

	public String getImageFileName(String parentPath, Long id) throws Exception {
		String filePath = parentPath + "/" + id + ".png";
		return filePath;
	}

	public String uploadImage(String parentPath, Long id, MultipartFile file) throws Exception {
		String filePath = getImageFileName(parentPath, id);
		MinioClientUtils.getInstance().putObject(filePath, file.getInputStream());
		return filePath;
	}
}
