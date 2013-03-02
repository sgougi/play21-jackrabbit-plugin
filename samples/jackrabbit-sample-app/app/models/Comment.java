package models;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;


@Node(extend=AbstractNode.class)
public class Comment extends AbstractNode
{

	@Field private String content;

    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	

}
