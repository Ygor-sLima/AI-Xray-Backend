package br.com.aixray.apixray.Models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

public class Imagem {
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String pathImagem;
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String viewPosition;
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Integer originalImageWidth;
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Integer originalImageHeight;
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Float originalImagePixelSpacingX;
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Float originalImagePixelSpacingY;

    public Imagem() {
    }

    public Imagem(String pathImagem, String viewPosition, Integer originalImageWidth, Integer originalImageHeight, Float originalImagePixelSpacingX, Float originalImagePixelSpacingY) {
        this.pathImagem = pathImagem;
        this.viewPosition = viewPosition;
        this.originalImageWidth = originalImageWidth;
        this.originalImageHeight = originalImageHeight;
        this.originalImagePixelSpacingX = originalImagePixelSpacingX;
        this.originalImagePixelSpacingY = originalImagePixelSpacingY;
    }

    public String getPathImagem() {
        return pathImagem;
    }

    public void setPathImagem(String pathImagem) {
        this.pathImagem = pathImagem;
    }

    public String getViewPosition() {
        return viewPosition;
    }

    public void setViewPosition(String viewPosition) {
        this.viewPosition = viewPosition;
    }

    public Integer getOriginalImageWidth() {
        return originalImageWidth;
    }

    public void setOriginalImageWidth(Integer originalImageWidth) {
        this.originalImageWidth = originalImageWidth;
    }

    public Integer getOriginalImageHeight() {
        return originalImageHeight;
    }

    public void setOriginalImageHeight(Integer originalImageHeight) {
        this.originalImageHeight = originalImageHeight;
    }

    public Float getOriginalImagePixelSpacingX() {
        return originalImagePixelSpacingX;
    }

    public void setOriginalImagePixelSpacingX(Float originalImagePixelSpacingX) {
        this.originalImagePixelSpacingX = originalImagePixelSpacingX;
    }

    public Float getOriginalImagePixelSpacingY() {
        return originalImagePixelSpacingY;
    }

    public void setOriginalImagePixelSpacingY(Float originalImagePixelSpacingY) {
        this.originalImagePixelSpacingY = originalImagePixelSpacingY;
    }

    @Override
    public String toString() {
        return "Imagem{" +
                "pathImagem='" + pathImagem + '\'' +
                ", viewPosition='" + viewPosition + '\'' +
                ", originalImageWidth=" + originalImageWidth +
                ", originalImageHeight=" + originalImageHeight +
                ", originalImagePixelSpacingX=" + originalImagePixelSpacingX +
                ", originalImagePixelSpacingY=" + originalImagePixelSpacingY +
                '}';
    }
}
