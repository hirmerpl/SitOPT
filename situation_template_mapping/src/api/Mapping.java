package api;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import mapping.Mapper;
import situationtemplate.model.TSituationTemplate;

/**
 * Class implementing the interface of the mapping
 */
public class Mapping implements MappingInterface {
	
	/**
	 * Invokes the mapping of the situation template and the deployment to Node-RED
	 * 
	 * @param situationTemplatePath
	 * 				 the path to the situation template XML as string
	 * @param doOverwrite
	 * 				 determines whether the currently deployed flows shall be overwritten 
	 */
	@Override
	public void mapAndDeploy(String situationTemplatePath, boolean doOverwrite) {
		try {
			// input is defined, parse the XML model
			JAXBContext jc;
			jc = JAXBContext.newInstance(TSituationTemplate.class);

			Unmarshaller u = jc.createUnmarshaller();
			File file = new File(situationTemplatePath);
			JAXBElement<TSituationTemplate> root = u.unmarshal(new StreamSource(file), TSituationTemplate.class);

			TSituationTemplate situationTemplate = root.getValue();

			Mapper mapper = new Mapper(situationTemplate);
			mapper.map(doOverwrite);

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
