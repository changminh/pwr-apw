/*
 *  Copyright (c) 2009, Wrocław University of Technology
 *  All rights reserved.
 *  Redistribution  and use in source  and binary  forms,  with or
 *  without modification,  are permitted provided that the follow-
 *  ing conditions are met:
 *
 *   • Redistributions of source code  must retain the above copy-
 *     right  notice, this list  of conditions and  the  following
 *     disclaimer.
 *   • Redistributions  in binary  form must  reproduce the  above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the  documentation and/or other mate provided
 *     with the distribution.
 *   • Neither  the name of the  Wrocław University of  Technology
 *     nor the names of its contributors may be used to endorse or
 *     promote products derived from this  software without speci-
 *     fic prior  written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRI-
 *  BUTORS "AS IS" AND ANY  EXPRESS OR IMPLIED WARRANTIES, INCLUD-
 *  ING, BUT NOT  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTA-
 *  BILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 *  NO EVENT SHALL THE COPYRIGHT HOLDER OR  CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCURE-
 *  MENT OF SUBSTITUTE  GOODS OR SERVICES;  LOSS OF USE,  DATA, OR
 *  PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER  CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING  NEGLIGENCE  OR  OTHERWISE) ARISING IN ANY WAY
 *  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSI-
 *  BILITY OF SUCH DAMAGE.
 */
package apw.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * This file returns a Properties reference, 
 *
 * @author Waldemar Szostak < wszostak@wp.pl >
 */
public final class PropertiesManager {
//	public static Properties getProperties(String fileName) {
//		final ClassLoader loader = PropertiesManager.class.getClassLoader();
//		final Properties properties = new Properties();
//		BufferedInputStream bufferedStream = null;
//
//		fileName = "/home/wsz/NetBeansProjects/pwr-apw/APW/src/META-INF/" + fileName;
//
//		/* First, we have to be able to read in default */
//		try {
//			bufferedStream = new BufferedInputStream(loader.getResourceAsStream(fileName + ".defaults.properties"));
//
//			properties.load(bufferedStream);
//		} catch (IOException e) {
//			throw new RuntimeException("The default properties file '" + fileName + ".defaults.properties' could not be opened", e);
//		} finally {
//			try {
//				if (bufferedStream != null) {
//					bufferedStream.close();
//				}
//			} catch (IOException e) {
//				//ignoring
//			}
//		}
//
//		/* Secondly, we should only TRY to load custom values */
//		try {
//			bufferedStream = new BufferedInputStream(loader.getResourceAsStream(fileName + ".properties"));
//			properties.load(bufferedStream);
//		} catch (IOException e) {
//			// It's acceptable that there's no custom properties file available
//		}
//
//		return properties;
//	}

	public static Properties getProperties(String fileName) {
		final Properties properties = new Properties();
		BufferedInputStream bufferedStream = null;

		fileName = "/home/wsz/NetBeansProjects/pwr-apw/APW/src/META-INF/" + fileName;

		/* First, we have to be able to read in default */
		try {
			bufferedStream = new BufferedInputStream(new FileInputStream(fileName + ".defaults.properties"));

			properties.load(bufferedStream);
		} catch (IOException e) {
			throw new RuntimeException("The default properties file '" + fileName + ".defaults.properties' could not be opened", e);
		} finally {
			try {
				if (bufferedStream != null) {
					bufferedStream.close();
				}
			} catch (IOException e) {
				//ignoring
			}
		}

		/* Secondly, we should only TRY to load custom values */
		try {
			bufferedStream = new BufferedInputStream(new FileInputStream(fileName + ".properties"));
			properties.load(bufferedStream);
		} catch (IOException e) {
			// It's acceptable that there's no custom properties file available
		}

		return properties;
	}

	public static void main(String[] args) {
		PropertiesManager.getProperties("featureSelectingGA");
	}
}
