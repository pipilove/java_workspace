package DataPreProcess.FeatureCalculate;

public class DocAlike {
	/** VSM计算文档相似度，返回的矩阵中的元素 */
	public double[][] CosCalculateWholeMatrix_weight(double[][] matrix) {
		double[][] result = new double[matrix.length][matrix.length];
		int i, j, k;
		for (i = 0; i < matrix.length; i++)
			for (j = 0; j <= i; j++) {//当前文档跟序号比他小的文档计算相似度
				result[i][j] = 0;
				for (k = 0; k < matrix[0].length; k++)//matrix[i].length改为了matrix[0].length
					result[i][j] = result[i][j] + matrix[i][k] * matrix[j][k];
				result[i][j] = result[i][j] / MatrixSqrt(matrix[i], matrix[j]);
			}
		return result;
	}

	private double MatrixSqrt(double[] ds, double[] ds2) {
		int i;
		double sum_1 = 0, sum_2 = 0;
		for (i = 0; i < ds.length; i++)
			sum_1 = ds[i] * ds[i] + sum_1;
		for (i = 0; i < ds2.length; i++)
			sum_2 = ds2[i] * ds2[i] + sum_2;
		return Math.sqrt(sum_1 * sum_2);
	}
}
