/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School Alessandro
 * Rigano (Program in Molecular Medicine) Caterina Strambio De Castillia
 * (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo Sbalzarini and Mario Valle.
 *
 * Key contacts: Caterina Strambio De Castillia: caterina.strambio@umassmed.edu
 * Alex Rigano: alex.rigano@umassmed.edu
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package edu.umassmed.omega.commons.data.imageDBConnectionElements;

public abstract class OmegaGateway {

	public abstract boolean isConnected();

	public abstract void setConnected(boolean isConnected);

	public abstract int connect(final OmegaLoginCredentials loginCred,
			final OmegaServerInformation serverInfo) throws Exception;
	
	public abstract byte[] getImageData(final Long pixelsID, final int z,
			final int t, final int c) throws Exception;
	
	public abstract int getDefaultZ(final Long pixelsID) throws Exception;
	
	public abstract void setDefaultZ(final Long pixelsID, int z)
			throws Exception;
	
	public abstract int getDefaultT(final Long pixelsID) throws Exception;
	
	public abstract void setDefaultT(final Long pixelsID, int t)
			throws Exception;
	
	public abstract int[] renderAsPackedInt(final Long pixelsID)
			throws Exception;
	
	public abstract int[] renderAsPackedInt(final Long pixelsID, int t, int z)
			throws Exception;
	
	public abstract byte[] renderCompressed(final Long pixelsID)
			throws Exception;
	
	public abstract byte[] renderCompressed(final Long pixelsID, int t, int z)
			throws Exception;
	
	public abstract void setActiveChannel(final Long pixelsID, int channel,
			boolean active) throws Exception;
	
	public abstract Double computeSizeT(final Long pixelsID, int sizeT,
			int currentMaxT) throws Exception;
	
	public abstract void setCompressionLevel(final Long pixelsID,
			float compression) throws Exception;
	
	public abstract double getDeltaT(final Long pixelsID, final int z,
			final int t, final int c) throws Exception;
	
	public abstract int getByteWidth(Long pixelsID) throws Exception;
}
