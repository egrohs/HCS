package hstools;

import hstools.domain.entities.Node;

public class EntityService extends GenericService<Node> {
	@Override
	Class<Node> getEntityType() {
		return Node.class;
	}
}