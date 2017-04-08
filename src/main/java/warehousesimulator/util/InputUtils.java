package warehousesimulator.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility methods for reading from streams.
 */
public class InputUtils {

  /**
   * Read lines from a stream to a list of strings.
   * @param stream the stream to read from
   * @return The contents of a stream as a list of strings.
   * @throws IOException if an IO error occurs while reading from the stream
   */
  public static List<String> readLinesFromStream(InputStream stream) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      return reader.lines().collect(Collectors.toList());
    }
  }

}
