package entityClasses;
/*******
 * <p> Title: Reply Class </p>
 * 
 * <p> Description: This Reply class represents a reply entity in the system.  It contains the replies
 *  details such as replyId, parentId, and contents. </p>
 * 
 * <p> Copyright: Noah Cilliers © 2026 </p>
 * 
 * @author Noah Cillers
 * 
 * 
 */
public class Reply {

    private long replyId;
    private long parentPostId;
    private String authorUsername;
    private String contents;
  
    public Reply(long replyId, long parentPostId,
                 String authorUsername, String content) {

        this.replyId = replyId;
        this.parentPostId = parentPostId;
        this.authorUsername = authorUsername;
        this.contents = content;
    }

    public long getParentPostId() { return parentPostId; }
    public long getReplyId() { return replyId; }
    public String getAuthorUsername() { return authorUsername; }
    public String getContent() { return contents; }
    public void setContent(String s){contents = s;}
    public void setAuthorUsername(String s) {authorUsername =s ;}
}
