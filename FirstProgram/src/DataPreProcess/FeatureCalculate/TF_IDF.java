package DataPreProcess.FeatureCalculate;

public class TF_IDF {
	// 计算词语TF值（词频：即每个文档中词的频率）
	public double[][] TF_Calcu(int[][] matrix) {
		int Doc_i, Term_j;// 文档编号，词编号
		double[] Doc_sum = new double[matrix.length];//每个文档特征词的数量
		
		// 计算每个文档词的数量总和
		for (Doc_i = 0; Doc_i < Doc_sum.length; Doc_i++) {
			Doc_sum[Doc_i] = 0;
			for (Term_j = 0; Term_j < matrix[Doc_i].length; Term_j++)
				if (matrix[Doc_i][Term_j] != 0)
					Doc_sum[Doc_i] += matrix[Doc_i][Term_j];
		}

		// 得到TF_matrix
		double[][] TF_matrix = new double[matrix.length][matrix[0].length];
		for (Doc_i = 0; Doc_i < TF_matrix.length; Doc_i++)
			for (Term_j = 0; Term_j < TF_matrix[Doc_i].length; Term_j++)
				if (matrix[Doc_i][Term_j] != 0)
					TF_matrix[Doc_i][Term_j] = matrix[Doc_i][Term_j]/ Doc_sum[Doc_i];
		return TF_matrix;
	}

	// 计算IDF	不同文档特征词相同（matrix同一列）不为0的IDF都是一样的
	public double[] IDF_Calcu(int[][] matrix) {
		double[] IDF_matrix = new double[matrix[0].length];
		int Doc_i, Term_j, Term_count;//Term_count即包含词的文档的数目
		// 得到包含词的文档的数目
		for (Term_j = 0; Term_j < IDF_matrix.length; Term_j++) {
			Term_count = 1;
			for (Doc_i = 0; Doc_i < matrix.length; Doc_i++)
				if (matrix[Doc_i][Term_j] != 0)
					Term_count++;
			IDF_matrix[Term_j] = Term_count;
			IDF_matrix[Term_j] = Math.log(matrix.length / IDF_matrix[Term_j]);//所有(语料库中）文件总数/包含特征词的文件数目；Math.log直接调用
		}
		return IDF_matrix;
	}

	// 计算TF_IDF
	public double[][] TF_IDF_Calcu(int[][] matrix) {
		double[][] TF = TF_Calcu(matrix);
		double[] IDF = IDF_Calcu(matrix);
		double[][] TF_IDF_matrix = new double[matrix.length][matrix[0].length];
		int Doc_i, Term_j;
		for (Doc_i = 0; Doc_i < matrix.length; Doc_i++)
			for (Term_j = 0; Term_j < matrix[0].length; Term_j++)//matrix[Doc_i].length改为matrix[0].length
				TF_IDF_matrix[Doc_i][Term_j] = TF[Doc_i][Term_j] * IDF[Term_j];
		return TF_IDF_matrix;
	}
}
