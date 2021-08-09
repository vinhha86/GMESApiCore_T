package vn.gpay.gsmart.core.dictionary_type;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IDictionaryType_Service extends Operations<DictionaryType> {
	public List<DictionaryType>loadDictionaryType();
}
