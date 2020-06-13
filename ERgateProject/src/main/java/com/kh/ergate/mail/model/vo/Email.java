package com.kh.ergate.mail.model.vo;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Email {
	private int mailNo;
	private String mailImportFlag;
	private String mailFrom;
	private String mailnameFrom;
	private String mailTo;
	private String mailnameTo;
	private String mailTitle;
	private String mailContent;
	private String mailAttachment;
	private Date mailDate;
	private String mailDateStr;
	private String mailReadFlag;
	private String mailStatus;
}
