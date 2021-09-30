package vn.gpay.gsmart.core.dictionary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.dictionary_type.DictionaryType;

@Table(name = "dictionary")
@Entity
public class Dictionary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dictionary_generator")
	@SequenceGenerator(name = "dictionary_generator", sequenceName = "dictionary_id_seq", allocationSize = 1)
	private Long id;
	private Long dictionary_typeid_link;
	private String question;
	private String answer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	// thêm trường lại từ điển
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "dictionary_typeid_link", insertable = false, updatable = false)
	private DictionaryType dictionary_type;

	@Transient
	public String getName() {
		if (dictionary_type != null) {
			return dictionary_type.getName();
		}
		return "";
	}

	public Long getDictionary_typeid_link() {
		return dictionary_typeid_link;
	}

	public void setDictionary_typeid_link(Long dictionary_typeid_link) {
		this.dictionary_typeid_link = dictionary_typeid_link;
	}
}
