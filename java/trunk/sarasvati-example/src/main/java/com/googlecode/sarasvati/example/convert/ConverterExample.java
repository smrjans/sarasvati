package com.googlecode.sarasvati.example.convert;

import java.util.Arrays;

import com.googlecode.sarasvati.env.AttributeConverter;
import com.googlecode.sarasvati.env.AttributeConverters;

public class ConverterExample
{
  public static void main(String[] args)
  {
    AttributeConverter longArrayConverter = new AttributeConverter()
    {      
      @Override
      public Object stringToObject(String string, Class<?> type)
      {
        if (string == null)
        {
          return null;
        }
                
        if (!type.isArray() ||
            type.getComponentType() != Long.TYPE)
        {
          throw new IllegalArgumentException("This converter can only be used to convert string to long[]");
        }        
        
        if ("[]".equals(string))
        {
          return new long[0];
        }
        
        final String[] elements = string.substring(1, string.length() - 1).split(",");
        final long[] result = new long[elements.length]; 
        
        for (int i = 0; i < elements.length; i++)
        {
          result[i] = Long.parseLong(elements[i]);
        }
        
        return result; 
      }
      
      @Override
      public String objectToString(Object object)
      {
        if (object == null)
        {
          return null;
        }
        
        if (!object.getClass().isArray() ||
            object.getClass().getComponentType() != Long.TYPE)
        {
          return null;
        }
        
        final long[] array = (long[])object;
                
        final StringBuilder buf = new StringBuilder(3 * array.length);
        buf.append("[");
        if (array.length > 0)
        {
          buf.append(array[0]);
        }
        for (int i = 1; i < array.length; i++)
        {
          buf.append(",");
          buf.append(array[i]);
        }
        buf.append("]");
        
        return buf.toString();
      }
    };
    
    @SuppressWarnings("unchecked")
    final Class<long[]> longArrayType = (Class<long[]>) new long[0].getClass();
    
    AttributeConverters.setConverterForType(longArrayType, longArrayConverter);
    
    long[][] testArrays = 
        new long[][] { null,
                       {},
                       {1},
                       {98754},
                       {1, 2, 3, 4, 5, 6, 7, 8, 9, 0},
                       {43124,43141,43143,4314,34,12343,4,143,43,41,43,43,6,16,346,4,5,315,345,2}};

    for (int i = 0; i < testArrays.length; i++)
    {
      long[] testArray = testArrays[i];
      System.out.println("\n  Orig array: " + Arrays.toString(testArray));
      String serialized = AttributeConverters.objectToString(testArray);
      System.out.println("  Serialized: " + serialized);
      long[] deserialized = AttributeConverters.stringToObject(serialized, longArrayType);
      System.out.println("Deserialized: " + Arrays.toString(deserialized));
      System.out.println("Equals? " + Arrays.equals(testArray, deserialized));
    }
  }
}
