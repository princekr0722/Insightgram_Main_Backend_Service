package com.Insightgram.services;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContentByteAndType {
	byte[] mediaBytes;
	String mediaType;
}
