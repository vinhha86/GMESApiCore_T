package vn.gpay.gsmart.core.documentguide;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDocumentGuide_Service extends Operations<DocumentGuide>{

	List<DocumentGuide> loadByType(Integer doctype);

}
