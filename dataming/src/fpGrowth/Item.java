package fpGrowth;
import java.util.ArrayList;
import java.util.List;
//List�ӿڵĳ���ʵ������ArrayList��LinkedList����ʹ��List����ʱ��ͨ�����������ΪList���ͣ�ʵ����ʱ����ʵ���������Ҫ��ʵ����ΪArrayList��LinkedList
public class Item implements Comparable {
//�ڵ���
/*�ӿ�ǿ�ж�ʵ������ÿ����Ķ���������������������򱻳�Ϊ�����Ȼ������� compareTo ��������Ϊ������Ȼ�ȽϷ���
 * ʵ��Comparable�ӿڵĶ����б������飩����ͨ�� Collections.sort���� Arrays.sort�������Զ�����
 * �����ɷ������ʵ��ʱ��û��ǿ��Ҫ��Ҫȥ���������Ͳ�����ֻ�ǲ��������Ͳ���ʱ����������棺
 * ��"Test is a raw type. References to generic type Test<T> should be parameterized"*/
 private String value;
 private Item preItem; // ǰ�̽ڵ�Item
 private List<Item> nextItem = new ArrayList<Item>(); // �����ڵ�Item
 private Item sibling; // �����ڵ�,�ֵܽڵ�
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
 
 //�൱��ɨ���������ݿ⣬ÿ����Ʒ��Ƶ���ݼ�����
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