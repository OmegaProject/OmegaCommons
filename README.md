# **OmegaCommons API**

### Extraction of OMEGA libraries
OmegaCommons facilitates the extraction of OMEGA libraries for custom plugin development.
For this purpose it contains the following :
1. Java classes used as building blocks for the construction of individual plugins. 
2. Java interfaces necessary to mediate the communication between plugins and the core.
3. Components used for event-driven communication. 
4. All foundational libraries necessary for trajectory analysis in OMEGA: Such libraries are composed of math-oriented static Java classes, are sub-divided in five categories based on the tracking measure they are responsible for (i.e. Intensity, Mobility, Velocity, Diffusivity and Diffusivity Measures Uncertainty Estimation), and are specifically designed to facilitate the generation of stand-alone Tracking Measures tools. 
5. Generic GUI elements, as well as common constants and exceptions were included here to facilitate the application accessibility by third-party plugins.

### Data Structures
In addition, Commons API contains three categories of data structures that are designed to store image data and metadata as well as the results produced at run time by the OMEGA application: 
1. The first group includes structures designed to contain data and metadata originating from OME-XML specific elements, such as Project, Dataset, Image, Experimenter and Experimenter Group. 
2. The second group of data structures was created on the basis of the OMEGA data model and MIAPTE specifications to store data produced as output by the analysis pipeline. Since most of OMEGA data-producing processes require parameter input, these data structures are constructed to allow the storage of both results and parameter settings associated with each analysis runs. 
3. The third group contains generic data structures whose purpose is, for example, to store data necessary to establishing connections with remote data stores, such as server information (i.e. hostname and port) and user credentials (i.e. username, encrypted password). This arrangement allows developers to extend or implement each components thereby facilitating the development of third-party plugins for connecting OMEGA to servers other than OMERO and OMEGA.
