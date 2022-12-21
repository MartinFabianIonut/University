package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository0;

import java.io.*;

import java.util.Arrays;
import java.util.List;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository0<ID, E extends Entity<ID>> extends InMemoryRepository0<ID,E> {
    String fileName;
    public AbstractFileRepository0(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
    }

    /**
     * Function for loading datas from files
     */
    public void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                super.save(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes is a string that contains what I have on my files - for users and friendships
     * @return an entity of type E
     */
    protected abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    /**
     *
     * @param entity -
     * @return a String for fine printing af entity of type E
     */
    protected abstract String createEntityAsString(E entity);

    /**
     *
     * @param entity
     *         entity must be not null
     * @return entity saved to file and in memory
     */
    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;
    }


    /**
     *
     * @param id of entity I want to delete from memory and file also
     *      id must be not null
     * @return deleted entity
     */
    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        if(e!=null){
            Iterable<E> eIterable = findAll();
            int i=0;
            for(E entity:eIterable){
                if(i==0){
                    rewriteToFile(entity);
                }
                else writeToFile(entity);
                i++;
            }
            if(i==0)
                try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,false))) {
                    bW.write("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
        return e;
    }

    /**
     *
     * @param entity I want to update
     *          entity must not be null
     * @return modified entity
     */
    @Override
    public E update(E entity) {
        E e=super.update(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;
    }

    /**
     * It writes an entity to file
     * @param entity I want to write to file
     */
    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rewriting to a file, so it writes from the beginning
     * @param entity that will be written to the beginning of a file
     */
    protected void rewriteToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,false))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

