/**
 * Created by:
 * - Lucas Kivi (kivix019)
 * - Charles Droege (droeg022)
 */

package utils;
import java.io.File;
import java.util.*;

public class NodeData {
   String address;
   int port;
   double prob;
   public NodeData(
       String address,
       double prob,
       int port     
   ) {
       this.address = address;
       this.port = port;
       this.prob = prob;
   }

   public int getPort() {
       return port;
   }

   public String getAddress() {
       return address;
   }

   public double getProbability() {
       return prob;
   }

}