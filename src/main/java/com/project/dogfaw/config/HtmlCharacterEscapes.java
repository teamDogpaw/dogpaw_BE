package com.project.dogfaw.config;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import org.apache.commons.text.StringEscapeUtils;

public class HtmlCharacterEscapes extends CharacterEscapes {
    private final int[] asciiEscapes;

    /*XSS(Cross-site Scripting) Filter*/
    /*스크립트를 이용한 공격방지*/
    public HtmlCharacterEscapes() {
        // xss 방지 처리할 특수 문자 지정
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }


    //이모지 관련 파싱에러 방지지
    @Override
    public SerializableString getEscapeSequence(int ch) {
        SerializedString serializedString = null;
        char charAt = (char) ch;

        if (Character.isHighSurrogate(charAt) || Character.isLowSurrogate(charAt)) {
            StringBuilder sb = new StringBuilder();
            sb.append("\\u");
            sb.append(String.format("%04x",ch));
            serializedString = new SerializedString(sb.toString());
        } else {
            serializedString =
                    new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString(charAt)));
        } return serializedString;
    }


}
