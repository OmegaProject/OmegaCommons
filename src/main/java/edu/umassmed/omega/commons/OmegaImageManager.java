package edu.umassmed.omega.commons;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OmegaImageManager {
	private OmegaGenericApplication app;
	private static OmegaImageManager instance = null;

	private final Map<Long, Map<Integer, Map<Integer, List<BufferedImage>>>> images;

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
		this.images = new LinkedHashMap<Long, Map<Integer, Map<Integer, List<BufferedImage>>>>();
	}

	public static List<BufferedImage> getImages(final Long id, final Integer c,
			final Integer z) {
		if ((c == null) || (z == null) || (z == -1))
			return null;
		Map<Integer, Map<Integer, List<BufferedImage>>> img = null;
		try {
			img = OmegaImageManager.instance.images.get(id);
		} catch (final Exception e) {
		}
		if (img == null)
			return null;
		Map<Integer, List<BufferedImage>> img2 = null;
		try {
			img2 = img.get(c);
		} catch (final Exception e) {
		}
		if (img2 == null)
			return null;
		List<BufferedImage> img3 = null;
		try {
			img3 = img2.get(z);
		} catch (final Exception e) {
		}

		return img3;
		// return OmegaImageManager.instance.images.get(id).get(c).get(z);
	}

	public static void saveImages(final Long id, final Integer c,
			final Integer z, final List<BufferedImage> images) {
		if ((c == null) || (z == null) || (z == -1))
			return;
		Map<Integer, List<BufferedImage>> map1 = null;
		Map<Integer, Map<Integer, List<BufferedImage>>> map2 = null;
		if (OmegaImageManager.instance.images.get(id) != null) {
			map2 = OmegaImageManager.instance.images.get(id);
		} else {
			map2 = new LinkedHashMap<Integer, Map<Integer, List<BufferedImage>>>();
		}
		if (map2.get(c) != null) {
			map1 = OmegaImageManager.instance.images.get(id).get(c);
		} else {
			map1 = new LinkedHashMap<Integer, List<BufferedImage>>();
		}
		map1.put(z, images);
		map2.put(c, map1);
		OmegaImageManager.instance.images.put(id, map2);
	}
	
	public static void clearImagesForID(final Long id, final Integer c,
			final Integer z) {
		if ((c == null) || (z == null) || (z == -1))
			return;
		Map<Integer, List<BufferedImage>> map1 = null;
		Map<Integer, Map<Integer, List<BufferedImage>>> map2 = null;
		if (OmegaImageManager.instance.images.get(id) != null) {
			map2 = OmegaImageManager.instance.images.get(id);
		}
		if ((map2 != null) && (map2.get(c) != null)) {
			map1 = OmegaImageManager.instance.images.get(id).get(c);
		}
		if ((map1 != null) && (map1.get(z) != null)) {
			map1.remove(z);
			map2.put(c, map1);
			OmegaImageManager.instance.images.put(id, map2);
		}
	}

	public static void clearImages() {
		OmegaImageManager.instance.images.clear();
	}
}
