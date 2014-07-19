package shell;


import java.lang.reflect.Field;
import java.util.List;
import java.util.LinkedList;


public class ReflectionUtil {
	
	public static Field[] getVariablesOfType(Class cls, Class type) {
		List<Field> fields = new LinkedList<Field>();
		while (cls != null) {
			Field[] results = cls.getDeclaredFields();
			for (int i=0;i<results.length;i++) {
				if (results[i].getType() == type) {
					fields.add(results[i]);
				}
			}
			cls = cls.getSuperclass();
		}
		return fields.toArray(new Field[fields.size()]);
	}
	
}
