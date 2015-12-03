package main.java.edu.umassmed.omega.commons;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OmegaImageManager {
	private OmegaGenericApplication app;
	private static OmegaImageManager instance = null;

	private final Map<Long, List<BufferedImage>> images;

	static {
		OmegaImageManager.instance = new OmegaImageManager();
	}

	public static void createNewOmegaFileManager(
	        final OmegaGenericApplication app) {
		OmegaImageManager.instance = new OmegaImageManager(app);
	}

	public static OmegaImageManager getOmegaImageManager(
	        final OmegaGenericApplication app) {
		OmegaImageManager.instance.app = app;
		return OmegaImageManager.instance;
	}

	public OmegaImageManager() {
		this(null);
	}

	private OmegaImageManager(final OmegaGenericApplication app) {
		this.app = app;
		this.images = new LinkedHashMap<Long, List<BufferedImage>>();
	}

	public static List<BufferedImage> getImages(final Long id) {
		return OmegaImageManager.instance.images.get(id);
	}

	public static void saveImages(final Long id,
			final List<BufferedImage> images) {
		OmegaImageManager.instance.images.put(id, images);
	}

	public static void clearImages() {
		OmegaImageManager.instance.images.clear();
	}
}
