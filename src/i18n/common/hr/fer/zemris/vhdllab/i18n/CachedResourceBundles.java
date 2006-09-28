package hr.fer.zemris.vhdllab.i18n;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class CachedResourceBundles {
	 
	private static Map<String,WeakReference<ResourceBundle>> cache = new
ConcurrentHashMap<String,WeakReference<ResourceBundle>>();
	public static ResourceBundle getBundle(String name) {
		return getBundle(name,null,null);
	}
 
	public static ResourceBundle getBundle(String name, String language) {
		return getBundle(name,language,null);
	}
 
	public static ResourceBundle getBundle(String name, String language,
String country) {
		String key = "N="+name+"|L="+language+"|C="+country;
		WeakReference ref = cache.get(key);
		ResourceBundle bundle = null;
		if(ref!=null) {
			bundle = (ResourceBundle)ref.get();
		}
		if(bundle!=null) {
			return bundle;
		}
		synchronized (cache) {
			ref = cache.get(key);
			bundle = null;
			if(ref!=null) {
				bundle = (ResourceBundle)ref.get();
			}
			if(bundle!=null) return bundle;
			bundle = loadBundle(name, language,country);
			cache.put(key,new WeakReference<ResourceBundle>(bundle));
			return bundle;
		}
	}
 
	private static ResourceBundle loadBundle(String name, String language,
String country) {
		ResourceBundle bundle;
		if(language==null) {
			bundle = ResourceBundle.getBundle(name);
		} else {
			if(country==null) {
				bundle = ResourceBundle.getBundle(name,new Locale(language));
			} else {
				bundle = ResourceBundle.getBundle(name,new Locale(language,country));
			}
		}
		return bundle;
	}
}
