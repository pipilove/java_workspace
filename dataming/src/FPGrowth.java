public void FPGrowth(List<List<String>> transRecords, List<String> postPattern,Context context) throws IOException, InterruptedException {
    // 构建项头表，同时也是频繁1项集
    ArrayList<TreeNode> HeaderTable = buildHeaderTable(transRecords);
    // 构建FP-Tree
    TreeNode treeRoot = buildFPTree(transRecords, HeaderTable);
    // 如果FP-Tree为空则返回
    if (treeRoot.getChildren()==null || treeRoot.getChildren().size() == 0)
        return;
    //输出项头表的每一项+postPattern
    if(postPattern!=null){
        for (TreeNode header : HeaderTable) {
            String outStr=header.getName();
            int count=header.getCount();
            for (String ele : postPattern)
                outStr+="\t" + ele;
            context.write(new IntWritable(count), new Text(outStr));
        }
    }
    // 找到项头表的每一项的条件模式基，进入递归迭代
    for (TreeNode header : HeaderTable) {
        // 后缀模式增加一项
        List<String> newPostPattern = new LinkedList<String>();
        newPostPattern.add(header.getName());
        if (postPattern != null)
            newPostPattern.addAll(postPattern);
        // 寻找header的条件模式基CPB，放入newTransRecords中
        List<List<String>> newTransRecords = new LinkedList<List<String>>();
        TreeNode backnode = header.getNextHomonym();
        while (backnode != null) {
            int counter = backnode.getCount();
            List<String> prenodes = new ArrayList<String>();
            TreeNode parent = backnode;
            // 遍历backnode的祖先节点，放到prenodes中
            while ((parent = parent.getParent()).getName() != null) {
                prenodes.add(parent.getName());
            }
            while (counter-- > 0) {
                newTransRecords.add(prenodes);
            }
            backnode = backnode.getNextHomonym();
        }
        // 递归迭代
        FPGrowth(newTransRecords, newPostPattern,context);
    }
}
