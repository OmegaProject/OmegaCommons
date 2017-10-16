package edu.umassmed.omega.commons.data.trajectoryElements;

import java.awt.Color;

import edu.umassmed.omega.commons.data.coreElements.OmegaNamedElement;

public class OmegaSegmentationType extends OmegaNamedElement {
	
	private static String DISPLAY_NAME = "Segmentation Type";

	public static final String NOT_ASSIGNED = "Not assigned";
	public static final Color NOT_ASSIGNED_COL = Color.black;
	public static final Integer NOT_ASSIGNED_VAL = 0;
	public static final String NOT_ASSIGNED_DESC = "";

	public static final String DIRECTED = "Directed";
	public static final Color DIRECTED_COL = new Color(0xA52A2A);
	public static final Integer DIRECTED_VAL = 1;
	public static final String DIRECTED_DESC = "'Directed motion combines motion at a given velocity with diffusion, as in diffusion in a flowing membrane or cytoplasm, or directed motion along a flexible macromolecule.' Saxton. In: Fundamental Concepts in Biophysics. 2009";

	public static final String CONFINED = "Confined";
	public static final Color CONFINED_COL = new Color(0xFFA500);
	public static final Integer CONFINED_VAL = 2;
	public static final String CONFINED_DESC = "'Confined diffusion appears normal at short times, but at long times MSD approaches the square of the radius of the confinement region. For membrane proteins, confinement could be in [...] a region where cytoskeletal proteins near the membrane form a corral. For nuclear or cytoplasmic proteins, confinement could be in a dense region of chromatin or cytoskeletal proteins. Tethering to a fixed point would give similar motion.' Saxton. In: Fundamental Concepts in Biophysics. 2009";

	public static final String SUB_DIFFUSIVE = "Anomalous subdiffusion";
	public static final Color SUB_DIFFUSIVE_COL = new Color(0xFF00FF);
	public static final Integer SUB_DIFFUSIVE_VAL = 3;
	public static final String SUB_DIFFUSIVE_DESC = "'In anomalous subdiffusion, hindrances to diffusion are extreme enough that the mean-square displacement (MSD) becomes proportional to some power of time less than one. The diffusion coefficient is time-dependent, and goes to zero at large times.' adapted from Saxton. In: Fundamental Concepts in Biophysics. 2009";

	public static final String DIFFUSIVE = "Normal diffusion";
	public static final Color DIFFUSIVE_COL = new Color(0x0000FF);
	public static final Integer DIFFUSIVE_VAL = 4;
	public static final String DIFFUSIVE_DESC = "'In normal diffusion, the mean-square displacement (MSD) is directly proportional to time and the diffusion coefficient is constant.' Saxton. In: Fundamental Concepts in Biophysics. 2009";

	public static final String SUPER_DIFFUSIVE = "Anomalous superdiffusion";
	public static final Color SUPER_DIFFUSIVE_COL = new Color(0x8A2BE2);
	public static final Integer SUPER_DIFFUSIVE_VAL = 5;
	public static final String SUPER_DIFFUSIVE_DESC = "In anomalous superdiffusion, the mean-square displacement (MSD) is proportional to some power of time more than one. This type of motion mimics a Levy flight and corresponds to diffusion accompanied by deterministic drift. For example, this might be observed when a subnuclear body diffuses withing a chromatin cage that is itself drifting";

	private Color color;
	private final Integer value;

	private boolean isChanged;
	
	private final String description;

	public OmegaSegmentationType(final String name, final Integer value,
			final Color color, final String description) {
		super(-1L, name);
		this.value = value;
		this.color = color;
		this.isChanged = false;
		this.description = description;
	}

	public boolean isChanged() {
		return this.isChanged;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setColor(final Color col) {
		this.color = col;
		this.isChanged = true;
	}

	public Color getColor() {
		return this.color;
	}

	public boolean isEqual(final OmegaSegmentationType segmType) {
		if (!segmType.getName().equals(this.getName()))
			return false;
		if (segmType.value != this.value)
			return false;
		if (segmType.color.getRed() != this.color.getRed())
			return false;
		if (segmType.color.getGreen() != this.color.getGreen())
			return false;
		if (segmType.color.getBlue() != this.color.getBlue())
			return false;
		return true;
	}

	public String getDescription() {
		return this.description;
	}

	public static String getStaticDisplayName() {
		return OmegaSegmentationType.DISPLAY_NAME;
	}

	@Override
	public String getDynamicDisplayName() {
		return OmegaSegmentationType.getStaticDisplayName();
	}
}
