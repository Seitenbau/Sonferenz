package org.apache.wicket.request.mapper;

import org.apache.wicket.request.component.IRequestablePage;


public class MountedMapper extends org.apache.wicket.core.request.mapper.MountedMapper {

	public MountedMapper(String mountPath,
			Class<? extends IRequestablePage> pageClass) {
		super(mountPath, pageClass);
	}

}
