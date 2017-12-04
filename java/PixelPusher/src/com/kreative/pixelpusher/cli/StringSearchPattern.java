package com.kreative.pixelpusher.cli;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.kreative.pixelpusher.device.StripInfo;
import com.kreative.pixelpusher.pixelset.PixelSet;
import com.kreative.pixelpusher.pixelset.PixelSetFactory;
import com.kreative.pixelpusher.pixelset.PixelSetInfo;

public class StringSearchPattern {
	private final Pattern pattern;
	
	public StringSearchPattern(String s) {
		StringBuffer sb = new StringBuffer();
		sb.append("^");
		int parenLevel = 0;
		CharacterIterator i = new StringCharacterIterator(s);
		for (char ch = i.first(); ch != CharacterIterator.DONE; ch = i.next()) {
			switch (ch) {
			case '{':
				sb.append("(");
				parenLevel++;
				break;
			case ',':
				sb.append((parenLevel > 0) ? "|" : ",");
				break;
			case '}':
				sb.append(")");
				parenLevel--;
				break;
			case '*':
				sb.append(".*?");
				break;
			case '?':
				sb.append(".");
				break;
			case ' ':
				if (!sb.toString().endsWith("\\s+")) sb.append("\\s+");
				break;
			case '(': case ')': case '[': case ']': case '\\':
			case '^': case '$': case '|': case '+': case '.':
				sb.append('\\');
				sb.append(ch);
				break;
			default:
				switch (Character.getType(ch)) {
				case Character.UPPERCASE_LETTER:
					sb.append('[');
					sb.append(ch);
					sb.append(Character.toTitleCase(ch));
					sb.append(Character.toLowerCase(ch));
					sb.append(']');
					break;
				case Character.TITLECASE_LETTER:
					sb.append('[');
					sb.append(Character.toUpperCase(ch));
					sb.append(ch);
					sb.append(Character.toLowerCase(ch));
					sb.append(']');
					break;
				case Character.LOWERCASE_LETTER:
					sb.append('[');
					sb.append(Character.toUpperCase(ch));
					sb.append(Character.toTitleCase(ch));
					sb.append(ch);
					sb.append(']');
					break;
				default:
					sb.append(ch);
					break;
				}
				break;
			}
		}
		sb.append("$");
		try {
			pattern = Pattern.compile(sb.toString());
		} catch (PatternSyntaxException e) {
			throw new GeneralParseException("Invalid string search pattern", s, e);
		}
	}
	
	public boolean matches(String s) {
		if (s == null) {
			return false;
		} else {
			return pattern.matcher(s).matches();
		}
	}
	
	public boolean matches(PixelPusher pusher) {
		return matches(pusher.getMacAddress())
			|| matches(pusher.getIp().getHostAddress())
			|| matches(pusher.getIp().getHostAddress() + ":" + pusher.getPort())
			|| matches("g" + pusher.getGroupOrdinal() + "c" + pusher.getControllerOrdinal())
			|| matches("u" + pusher.getArtnetUniverse() + "c" + pusher.getArtnetChannel());
	}
	
	public boolean matches(StripInfo info) {
		return matches(info.getName())
			|| (info.hasMacAddress() && matches(info.getMacAddress()))
			|| (info.hasIpAddress() && matches(info.getIpAddress()))
			|| (info.hasOrdinal() && matches("g" + info.getGroupOrdinal() + "c" + info.getControllerOrdinal()))
			|| (info.hasArtnet() && matches("u" + info.getArtnetUniverse() + "c" + info.getArtnetChannel()));
	}
	
	public boolean matches(PixelSetFactory<? extends PixelSet> factory) {
		return matches(factory.getNameForMachines())
			|| matches(factory.getNameForHumans())
			|| matches(factory.getClass().getName())
			|| matches(factory.getClass().getCanonicalName())
			|| matches(factory.getClass().getSimpleName())
			|| matches(factory.getPixelSetClass().getName())
			|| matches(factory.getPixelSetClass().getCanonicalName())
			|| matches(factory.getPixelSetClass().getSimpleName());
	}
	
	public boolean matches(PixelSetInfo<? extends PixelSet> info) {
		return matches(info.getName())
			|| matches(info.getPixelSetFactory());
	}
}
