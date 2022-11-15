package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository0;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFile0 extends AbstractFileRepository0<Long, Friendship> {
    private Repository0<Long, Utilizator> utilizatorRepository0;

//    public FriendshipFile0(String fileName, Validator<Friendship> validator) {
//        super(fileName, validator);
//    }

    public FriendshipFile0(String fileName, Validator<Friendship> validator, Repository0<Long, Utilizator> repository0) {
        super(fileName, validator);
        this.utilizatorRepository0 = repository0;
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        LocalDateTime date = LocalDateTime.parse(attributes.get(1));
        Utilizator u1 = utilizatorRepository0.findOne(Long.parseLong(attributes.get(2)));
        Utilizator u2 = utilizatorRepository0.findOne(Long.parseLong(attributes.get(3)));
        u1.getFriends().add(u2);
        u2.getFriends().add(u1);
        Friendship friendship = new Friendship(date,u1,u2);
        friendship.setId(Long.parseLong(attributes.get(0)));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId()+";"+entity.getDate()+";"+entity.getFirstFriend().getId()+";"+entity.getSecondFriend().getId();
    }
}
