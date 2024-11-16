package com.course.course.models;

import com.course.course.enums.CourseLevel;
import com.course.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Fetch Type- Corresponde a forma como vamos carregar os dados da base dados que pode ser:
 *          Lazy- carregamento lento onde não se traz tudo de uma vez (sem as tabelas que fazem chave estrangeira)
 *          Eager- Carregamento ansioso onde é carregado toda informacao independentemente se vai ser usado ou nao.(com as tabelas que fazem chave estrangeira)
 */

/**
 * Fecth Mode- Forma como vai buscar/selecionar os dados:
 *          SUBSELECT - Para esse mode el faz uma consulta para curso e mais uma para traser todos os modulos associados
 *          JOIN - Para esse mode ele vai fazer uma unica consulta com todos os modulos associados no join.
 *          SELECT - Para esse exemplo em concreto ele vai fazer um select para traser o curso e 1 select para cada um dos Móduleos relacionados com esse curso
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_COURSES")
public class CourseModel implements Serializable {

    private static final long serialVersionFinal = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false, length = 255)
    private String description;
    @Column
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;
    @Column(nullable = false)
    private UUID userInstructor; //utilisador que cria o course

    /*
     * Definir o tipo acesso neste caso e so de escrita
     */
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    /*
     * FLETCH TYPE
     * Definição da chave estrangeira. o nome "course" é nome do atributo
     * FecthType Lazy - Carregamento Lento. É melhor abordagem pq os dados são carregados qd necessários.
     * FecthType Eager - É feito o carregamento completo mesmo se os dados não vão ser usados.
     * A declaração dos FecthType é estático como está definido e para resolver isso devemos usar a dinamica do @EntityGraphs. Exemplo no ModuleRepositorie.
     *
     * DELETE:
     * CascadeType.ALL - Quando apagar um courso este vai pagar em cascata todos Modulos relacionados
     * orphanRemoval - Todos os modulos tem um courso. Se este comando tiver a true então ele vai pagar tambem todos modulos que não tem curso
     * Opção 1 não boa opcção pq vamos o jpa a executar 1 comando do delete do cousro e depois 1 comando para cada modulo e isto não seria pratico para performance de base dados
     * Opção 1: @OneToMany(mappedBy = "course",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
     * Opção 2: @OnDelete(action = OnDeleteAction.CASCADE) esta opção ele paga o cousro e depois a base dados vai apagar então as tabelas do modulo
     * Opção 2 tambem não boa opção pq o volume de dados pode por isso em causa e depois tb não temo controlo de como vai correr este processo.
     * Opção 3 Será feito de forma manual/medida para nos ajudar a salvaguardar bem as transações em caso de falhas para fazermos o roollback
     * e garantir que não haja perda de informação e para isso iremos implementar todos esses metodos que suporta essa acção . De todas é a melhor
     */
    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY)
    /*** Delete casacata opacao 1
     * @OneToMany(mappedBy = "course",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
     * Sempre que for pago um curso tb e pago os Model associados em cascata e com a propriedade orphanRemoval a tru significa que tb são pagos os model que não tem nenhuma relacao com curso.
     * Esta opção não boa por performance qd temos volumes grandes de dados porque o jpa para este caso el paga o curso e depois vai gerar tb um comando para pagar cada model o q não seria bom para bd.
     * Delete casacata opacao 2
     * @OnDelete(action = OnDeleteAction.CASCADE)
     * É um pouco melhor que a primeira opção. Normalmente o o JPA apaga o courso e depois delaga a responsabilidade para base dados pagar os modulos isto será um constrangimento porque perdemos
     * o controlo de como os dados estão a serem ou foram pagos na bd.
     *
     * **/

    /*
     * Ele vai faser 2 consulta um do curso e outro dos mdululos do curso.
     * @Fetch(FetchMode.JOIN) ele faz uma unica consulta para traser os dados do curso + os dados do model de uma vez so, usando join. Se não for usado nenhum Fletch Mode o JPA vai asumir por default JOIN
     * @Fetch(FetchMode.SELECT)
     */
    @Fetch(FetchMode.SUBSELECT)
    /*
     * Define a lista de modulos dentro do curso.
     * E preferivel usar Set que List pq qd usamos set normalmente numa pesquisa e retornado todos os grupos set e se usarmos List o hibernate usa apenas o a primeira List.
     * set não ordena e nao permite duplicação. List tem tudo contrario.
     */
    private Set<ModuleModel> modules;




}
