/**
 * 
 */
/**
 * @author prabhu
 *
 */
module esp8266_image_flasher {
	requires commons.exec;
	requires org.apache.commons.configuration2;
	requires org.apache.commons.io;
	requires java.logging;
	requires java.desktop;
	requires beansbinding;
	requires jssc;
	requires java.sql;
	exports org.jinjuamla.esp8266.imageflasher;
}