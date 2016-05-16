# AViewPager
ViewPager的代码品

设置items

addItemList(LinkedList<View> viewList)

设置某一个item

setItem(int position,View view)

插入一个item

addItem(int position,View view)

删除一个item

removeItem(int position)

可横向、竖向，以及不滑动

setOrientation(int orientation)

选择某一个item

setCurrentItem(int position)

选择某一个item，还可带动画

setCurrentItem(int position, int outAnim, int inAnim)
