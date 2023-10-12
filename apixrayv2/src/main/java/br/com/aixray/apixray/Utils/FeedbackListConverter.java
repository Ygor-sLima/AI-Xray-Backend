package br.com.aixray.apixray.Utils;

import br.com.aixray.apixray.Models.Feedback;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackListConverter implements DynamoDBTypeConverter<List<Map<String, AttributeValue>>, List<Feedback>> {

    @Override
    public List<Map<String, AttributeValue>> convert(List<Feedback> feedbacks) {
        List<Map<String, AttributeValue>> convertedFeedbacks = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            convertedFeedbacks.add(convertFeedbackToMap(feedback));
        }
        return convertedFeedbacks;
    }

    @Override
    public List<Feedback> unconvert(List<Map<String, AttributeValue>> items) {
        List<Feedback> feedbacks = new ArrayList<>();
        for (Map<String, AttributeValue> item : items) {
            feedbacks.add(unconvertMapToFeedback(item));
        }
        return feedbacks;
    }

    private Map<String, AttributeValue> convertFeedbackToMap(Feedback feedback) {
        Map<String, AttributeValue> itemMap = new HashMap<>();
        itemMap.put("retorno_modelo", new AttributeValue(feedback.getRetornoModelo()));
        itemMap.put("retorno_medico", new AttributeValue(feedback.getRetornoMedico()));
        itemMap.put("timestamp_inclusao", new AttributeValue().withN(String.valueOf(feedback.getTimestampInclusao())));
        itemMap.put("data_registro", new AttributeValue(feedback.getDataRegistro()));
        return itemMap;
    }

    private Feedback unconvertMapToFeedback(Map<String, AttributeValue> item) {
        String retornoModelo = item.get("retorno_modelo").getS();
        String retornoMedico = item.get("retorno_medico").getS();
        Integer timestampInclusao = Integer.parseInt(item.get("timestamp_inclusao").getN());
        String dataRegistro = item.get("data_registro").getS();

        return new Feedback(retornoModelo, retornoMedico, timestampInclusao, dataRegistro);
    }
}