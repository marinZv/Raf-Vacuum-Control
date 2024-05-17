package rs.raf.demo.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.raf.demo.domain.dto.vacuum.VacuumDto;
import rs.raf.demo.domain.dto.vacuum.VacuumErrorDto;
import rs.raf.demo.domain.entities.vacuum.Vacuum;
import rs.raf.demo.domain.entities.vacuum.VacuumError;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VacuumMapper {

    VacuumMapper INSTANCE = Mappers.getMapper(VacuumMapper.class);


    @Mapping(target = "createdDate", source = "vacuum", qualifiedByName = "getCreatedDate")
    VacuumDto vacuumToVacuumDto(Vacuum vacuum);


    @Mapping(target="dateError", source = "vacuumError", qualifiedByName = "getErrorDate")
    @Mapping(target="vacuum", source = "vacuumError", qualifiedByName = "getVacuumDto")
    VacuumErrorDto vacuumErrorToVacuumErrorDto(VacuumError vacuumError);

    @Named("getCreatedDate")
    default Long getCreatedDate(Vacuum vacuum){
        return vacuum.getCreatedDate().getTime();
    }

    @Named("getErrorDate")
    default Long getErrorDate(VacuumError vacuumError){
        return vacuumError.getDateError().getTime();
    }

    @Named("getVacuumDto")
    default VacuumDto getVacuumDto(VacuumError vacuumError){
        return vacuumToVacuumDto(vacuumError.getVacuum());
    }

}
