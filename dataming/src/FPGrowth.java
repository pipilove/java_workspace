public void FPGrowth(List<List<String>> transRecords, List<String> postPattern,Context context) throws IOException, InterruptedException {
    // ������ͷ��ͬʱҲ��Ƶ��1�
    ArrayList<TreeNode> HeaderTable = buildHeaderTable(transRecords);
    // ����FP-Tree
    TreeNode treeRoot = buildFPTree(transRecords, HeaderTable);
    // ���FP-TreeΪ���򷵻�
    if (treeRoot.getChildren()==null || treeRoot.getChildren().size() == 0)
        return;
    //�����ͷ���ÿһ��+postPattern
    if(postPattern!=null){
        for (TreeNode header : HeaderTable) {
            String outStr=header.getName();
            int count=header.getCount();
            for (String ele : postPattern)
                outStr+="\t" + ele;
            context.write(new IntWritable(count), new Text(outStr));
        }
    }
    // �ҵ���ͷ���ÿһ�������ģʽ��������ݹ����
    for (TreeNode header : HeaderTable) {
        // ��׺ģʽ����һ��
        List<String> newPostPattern = new LinkedList<String>();
        newPostPattern.add(header.getName());
        if (postPattern != null)
            newPostPattern.addAll(postPattern);
        // Ѱ��header������ģʽ��CPB������newTransRecords��
        List<List<String>> newTransRecords = new LinkedList<List<String>>();
        TreeNode backnode = header.getNextHomonym();
        while (backnode != null) {
            int counter = backnode.getCount();
            List<String> prenodes = new ArrayList<String>();
            TreeNode parent = backnode;
            // ����backnode�����Ƚڵ㣬�ŵ�prenodes��
            while ((parent = parent.getParent()).getName() != null) {
                prenodes.add(parent.getName());
            }
            while (counter-- > 0) {
                newTransRecords.add(prenodes);
            }
            backnode = backnode.getNextHomonym();
        }
        // �ݹ����
        FPGrowth(newTransRecords, newPostPattern,context);
    }
}
