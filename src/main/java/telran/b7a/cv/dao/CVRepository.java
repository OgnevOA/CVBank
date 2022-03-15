package telran.b7a.cv.dao;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import telran.b7a.cv.models.CV;

public interface CVRepository extends MongoRepository<CV, String> {

	Stream<CV> findBycvIdIn(List<String> cvsId);

	Stream<CV> findByCoordinatesNear(Point point, Distance distance);
	
	Stream<CV> findByPosition(String position);
	
	

//	@Aggregation(pipeline = {
//			"{$geoNear: {near : {type: 'Point', coordinates: [ ?0, ?1 ]}, distanceField: 'dist.calculated',\r\n"
//			+ "  maxDistance: ?2,\r\n"
//			+ "  query: {},\r\n"
//			+ "  spherical: true}}",
//			"{ $match : { $or: [{position : ?3}, {position : {$ne: null}} ] }}",  })
//	AggregationResults<CV> find(Double x, Double y, Integer i, String position);

}
