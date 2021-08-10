package vn.gpay.gsmart.core.dictionary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDictionary_Service extends Operations<Dictionary>{
	public List<Dictionary>loadDictionary();
}
