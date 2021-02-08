package ga.homealoneapi.src.user.models;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserInfoRes
{
    private final  String userName;
    private final  String addressName;
    private final PushStatus pushStatus;




}
