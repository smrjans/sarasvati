package com.googlecode.sarasvati.env;

import java.security.SecureRandom;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class Base64Test
{
  @Test
  public void test()
  {
    SecureRandom rand = new SecureRandom();

    for (int i = 1; i < 1024; i++)
    {
      byte[] array = new byte[i];
      rand.nextBytes(array);

      final String encoded = Base64.encode(array);

      byte[] decoded = Base64.decode(encoded);
      Assert.assertTrue(Arrays.equals(array, decoded));
    }

    for (int i = 1024; i < 100 * 1024; i += rand.nextInt(1024))
    {
      byte[] array = new byte[i];
      rand.nextBytes(array);

      final String encoded = Base64.encode(array);

      byte[] decoded = Base64.decode(encoded);
      Assert.assertTrue(Arrays.equals(array, decoded));
    }
  }
}
