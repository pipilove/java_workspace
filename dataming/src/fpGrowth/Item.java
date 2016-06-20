package fpGrowth;
import java.util.ArrayList;
import java.util.List;
//List接口的常用实现类有ArrayList和LinkedList，在使用List集合时，通常情况下声明为List类型，实例化时根据实际情况的需要，实例化为ArrayList或LinkedList
public class Item implements Comparable {
//节点类
/*接口强行对实现它的每个类的对象进行整体排序。这种排序被称为类的自然排序，类的 compareTo 方法被称为它的自然比较方法
 * 实现Comparable接口的对象列表（和数组）可以通过 Collections.sort（和 Arrays.sort）进行自动排序
 * 在生成泛型类的实例时，没有强制要求要去设置其类型参数，只是不设置类型参数时，会给出警告：
 * 如"Test is a raw type. References to generic type Test<T> should be parameterized"*/
 private String value;
 private Item preItem; // 前继节点Item
 private List<Item> nextItem = new ArrayList<Item>(); // 后续节点Item
 private Item sibling; // 关联节点,兄弟节点
 private int counter;
 public Item() {
 }
 public Item(String value) {
  this.value = value;
 }
 public void addCounter() {
  this.counter += 1;
 }
 public Item getSibling() {
  return sibling;
 }
 public void setSibling(Item sibling) {
  this.sibling = sibling;
 }
 public void addNextItem(Item item) {
  this.nextItem.add(item);
 }
 public List<Item> getNextItem() {
  return this.nextItem;
 }
 public String getValue() {
  return value;
 }
 public void setValue(String value) {
  this.value = value;
 }
 public Item getPreItem() {
  return preItem;
 }
 public void setPreItem(Item preItem) {
  this.preItem = preItem;
 }
 public int getCounter() {
  return counter;
 }
 public void setCounter(int counter) {
  this.counter = counter;
 }
 
 //相当于扫描事务数据库，每项商品按频数递减排序
 public int compareTo(Object o) {
  int value;
  Item i = (Item) o;
  if (this.counter > i.counter) {
   value = -1;
  } else if (this.counter == i.counter) {
   value = 0;
  } else {
   value = 1;
  }
  return value;
 }
}