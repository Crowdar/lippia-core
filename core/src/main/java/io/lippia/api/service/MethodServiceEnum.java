package io.lippia.api.service;

import com.crowdar.api.rest.MethodsService;

public enum MethodServiceEnum {

DEFAULT {
	@Override
	public Class<? extends MethodsService> getClazz() {
		return SimplifyMethodService.class;
	}
},
NOSSLVERIFICATION {
	@Override
	public Class<? extends MethodsService> getClazz() {
		return NoSSLVerificationMethodService.class;
	}
};
	
	public abstract Class<? extends MethodsService> getClazz();

}
