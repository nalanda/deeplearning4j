/*
 * Copyright 2015 Skymind,Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.arbiter.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Clusters strings based on fingerprint: for example
 * Two words and TWO words or WORDS TWO would be put together
 * @author Adam Gibson
 *
 */
public class StringCluster extends HashMap<String,Map<String,Integer>> {

    /**
     *
     */
    private static final long serialVersionUID = -4120559428585520276L;

    public StringCluster(List<String> list) {
      for (String s : list) {
        FingerPrintKeyer keyer = new FingerPrintKeyer();
        String key = keyer.key(s);
        if (containsKey(key)) {
          Map<String, Integer> m = get(key);
          if (m.containsKey(s)) {
            m.put(s, m.get(s) + 1);
          } else {
            m.put(s, 1);
          }
        } else {
          Map<String, Integer> m = new TreeMap<>();
          m.put(s, 1);
          put(key, m);
        }
      }
    }

    public List<Map<String,Integer>> getClusters() {
        List<Map<String,Integer>>_clusters = new ArrayList<>(values());
        Collections.sort(_clusters,new StringCluster.SizeComparator());
        return _clusters;
    }


    public void sort() {
        Collections.sort(new ArrayList<>(values()),new SizeComparator());
    }


    public static class SizeComparator implements Comparator<Map<String,Integer>>, Serializable {
        private static final long serialVersionUID = -1390696157208674054L;
        @Override
        public int compare(Map<String,Integer> o1, Map<String,Integer> o2) {
            int s1 = o1.size();
            int s2 = o2.size();
            if (o1 == o2) {
                int total1 = 0;
                for (int i : o1.values()) {
                    total1 += i;
                }
                int total2 = 0;
                for (int i : o2.values()) {
                    total2 += i;
                }
                return total2 - total1;
            } else {
                return s2 - s1;
            }
        }
    }

}