package DataPreProcess.FeatureCalculate;

public class TF_IDF {
	// �������TFֵ����Ƶ����ÿ���ĵ��дʵ�Ƶ�ʣ�
	public double[][] TF_Calcu(int[][] matrix) {
		int Doc_i, Term_j;// �ĵ���ţ��ʱ��
		double[] Doc_sum = new double[matrix.length];//ÿ���ĵ������ʵ�����
		
		// ����ÿ���ĵ��ʵ������ܺ�
		for (Doc_i = 0; Doc_i < Doc_sum.length; Doc_i++) {
			Doc_sum[Doc_i] = 0;
			for (Term_j = 0; Term_j < matrix[Doc_i].length; Term_j++)
				if (matrix[Doc_i][Term_j] != 0)
					Doc_sum[Doc_i] += matrix[Doc_i][Term_j];
		}

		// �õ�TF_matrix
		double[][] TF_matrix = new double[matrix.length][matrix[0].length];
		for (Doc_i = 0; Doc_i < TF_matrix.length; Doc_i++)
			for (Term_j = 0; Term_j < TF_matrix[Doc_i].length; Term_j++)
				if (matrix[Doc_i][Term_j] != 0)
					TF_matrix[Doc_i][Term_j] = matrix[Doc_i][Term_j]/ Doc_sum[Doc_i];
		return TF_matrix;
	}

	// ����IDF	��ͬ�ĵ���������ͬ��matrixͬһ�У���Ϊ0��IDF����һ����
	public double[] IDF_Calcu(int[][] matrix) {
		double[] IDF_matrix = new double[matrix[0].length];
		int Doc_i, Term_j, Term_count;//Term_count�������ʵ��ĵ�����Ŀ
		// �õ������ʵ��ĵ�����Ŀ
		for (Term_j = 0; Term_j < IDF_matrix.length; Term_j++) {
			Term_count = 1;
			for (Doc_i = 0; Doc_i < matrix.length; Doc_i++)
				if (matrix[Doc_i][Term_j] != 0)
					Term_count++;
			IDF_matrix[Term_j] = Term_count;
			IDF_matrix[Term_j] = Math.log(matrix.length / IDF_matrix[Term_j]);//����(���Ͽ��У��ļ�����/���������ʵ��ļ���Ŀ��Math.logֱ�ӵ���
		}
		return IDF_matrix;
	}

	// ����TF_IDF
	public double[][] TF_IDF_Calcu(int[][] matrix) {
		double[][] TF = TF_Calcu(matrix);
		double[] IDF = IDF_Calcu(matrix);
		double[][] TF_IDF_matrix = new double[matrix.length][matrix[0].length];
		int Doc_i, Term_j;
		for (Doc_i = 0; Doc_i < matrix.length; Doc_i++)
			for (Term_j = 0; Term_j < matrix[0].length; Term_j++)//matrix[Doc_i].length��Ϊmatrix[0].length
				TF_IDF_matrix[Doc_i][Term_j] = TF[Doc_i][Term_j] * IDF[Term_j];
		return TF_IDF_matrix;
	}
}
