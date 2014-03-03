package org.conventions.archetype.test.util;


import org.conventionsframework.qualifier.Config;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * resource bundle usado nos testes já que o bundle do framework é baseado no FacesContext e não temos o mesmo durante os testes
 */

@Config
public class TestResourceBundle extends java.util.PropertyResourceBundle implements Serializable {

  private static final long serialVersionUID = 1L;

  public TestResourceBundle() throws IOException {
      super(Thread.currentThread().getContextClassLoader().getResourceAsStream("messages_" + Locale.getDefault().getLanguage() + ".properties"));
      }

  public String getString(String key, Object... params) {
    return MessageFormat.format(this.getString(key), params);
  }

}
