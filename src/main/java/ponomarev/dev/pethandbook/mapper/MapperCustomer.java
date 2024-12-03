package ponomarev.dev.pethandbook.mapper;

public interface MapperCustomer<D, E> {

    D toDto(E entity);

    E toEntity(D dto);

}
