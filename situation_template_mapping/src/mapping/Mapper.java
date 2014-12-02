package mapping;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import situationtemplate.model.TSituationTemplate;
import utils.IOUtils;
import utils.NodeREDUtils;

/**
 * This is the entry point of the rule-based mapping library. The pattern-based
 * model will be transformed into a Node-RED-processable model.
 *
 */
public class Mapper {

	TSituationTemplate situationTemplate;

	public Mapper(TSituationTemplate situation) {
		this.situationTemplate = situation;
	}

	/**
	 * Main class of the mapping that receives the pattern-based model as JSON
	 * and invokes methods to transform it into an executable model.
	 * 
	 * @param jsonInput
	 *            the model to be transformed
	 */
	@SuppressWarnings("unchecked")
	public void map() {
		try {

			JSONArray nodeREDModel = new JSONArray();

			// each NodeRED flow needs an inject input node, which is generated
			// and added at this point
			JSONObject input = NodeREDUtils.generateInputNode(situationTemplate.getId(), situationTemplate);
			nodeREDModel.add(input);

			// first, map all the logic patterns, then map the other nodes
			LogicNodeMapper lnm = new LogicNodeMapper();
			lnm.mapLogicNodes(situationTemplate, nodeREDModel);
			
			SensorNodeMapper snm = new SensorNodeMapper();
			nodeREDModel = snm.mapSensorNodes(situationTemplate, nodeREDModel);
			
			NodeMapper nm = new NodeMapper();
			JSONArray finalModel = nm.mapNodes(situationTemplate, nodeREDModel);
			
			// write the JSON file (just for debug reasons) and deploy the JSON
			// model to NodeRED
			// TODO: call the deployToNodeRED method here
			IOUtils.writeJSONFile(finalModel, situationTemplate);

		} catch (ParseException e) {
			System.err.println("Could not parse JSON, an error occurred.");
			e.printStackTrace();
		}
	}
}