
package site.yvo11.ctranslate.Shanbay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("pronunciations")
    @Expose
    private Pronunciations pronunciations;
    @SerializedName("en_definitions")
    @Expose
    private EnDefinitions enDefinitions;
    @SerializedName("audio_addresses")
    @Expose
    private AudioAddresses audioAddresses;
    @SerializedName("uk_audio")
    @Expose
    private String ukAudio;
    @SerializedName("conent_id")
    @Expose
    private Integer conentId;
    @SerializedName("audio_name")
    @Expose
    private String audioName;
    @SerializedName("cn_definition")
    @Expose
    private CnDefinition cnDefinition;
    @SerializedName("num_sense")
    @Expose
    private Integer numSense;
    @SerializedName("content_id")
    @Expose
    private Integer contentId;
    @SerializedName("content_type")
    @Expose
    private String contentType;
    @SerializedName("sense_id")
    @Expose
    private Integer senseId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("definition")
    @Expose
    private String definition;
    @SerializedName("has_collins_defn")
    @Expose
    private Boolean hasCollinsDefn;
    @SerializedName("has_oxford_defn")
    @Expose
    private Boolean hasOxfordDefn;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("has_audio")
    @Expose
    private Boolean hasAudio;
    @SerializedName("en_definition")
    @Expose
    private EnDefinition enDefinition;
    @SerializedName("object_id")
    @Expose
    private Integer objectId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("pron")
    @Expose
    private String pron;
    @SerializedName("pronunciation")
    @Expose
    private String pronunciation;
    @SerializedName("id_str")
    @Expose
    private String idStr;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("us_audio")
    @Expose
    private String usAudio;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param pronunciations
     * @param senseId
     * @param audio
     * @param definition
     * @param audioName
     * @param numSense
     * @param pron
     * @param hasCollinsDefn
     * @param contentType
     * @param enDefinition
     * @param cnDefinition
     * @param enDefinitions
     * @param idStr
     * @param url
     * @param conentId
     * @param contentId
     * @param content
     * @param id
     * @param pronunciation
     * @param audioAddresses
     * @param ukAudio
     * @param hasAudio
     * @param objectId
     * @param hasOxfordDefn
     * @param usAudio
     */
    public Data(Pronunciations pronunciations, EnDefinitions enDefinitions, AudioAddresses audioAddresses, String ukAudio, Integer conentId, String audioName, CnDefinition cnDefinition, Integer numSense, Integer contentId, String contentType, Integer senseId, Integer id, String definition, Boolean hasCollinsDefn, Boolean hasOxfordDefn, String url, Boolean hasAudio, EnDefinition enDefinition, Integer objectId, String content, String pron, String pronunciation, String idStr, String audio, String usAudio) {
        super();
        this.pronunciations = pronunciations;
        this.enDefinitions = enDefinitions;
        this.audioAddresses = audioAddresses;
        this.ukAudio = ukAudio;
        this.conentId = conentId;
        this.audioName = audioName;
        this.cnDefinition = cnDefinition;
        this.numSense = numSense;
        this.contentId = contentId;
        this.contentType = contentType;
        this.senseId = senseId;
        this.id = id;
        this.definition = definition;
        this.hasCollinsDefn = hasCollinsDefn;
        this.hasOxfordDefn = hasOxfordDefn;
        this.url = url;
        this.hasAudio = hasAudio;
        this.enDefinition = enDefinition;
        this.objectId = objectId;
        this.content = content;
        this.pron = pron;
        this.pronunciation = pronunciation;
        this.idStr = idStr;
        this.audio = audio;
        this.usAudio = usAudio;
    }

    public Pronunciations getPronunciations() {
        return pronunciations;
    }

    public void setPronunciations(Pronunciations pronunciations) {
        this.pronunciations = pronunciations;
    }

    public EnDefinitions getEnDefinitions() {
        return enDefinitions;
    }

    public void setEnDefinitions(EnDefinitions enDefinitions) {
        this.enDefinitions = enDefinitions;
    }

    public AudioAddresses getAudioAddresses() {
        return audioAddresses;
    }

    public void setAudioAddresses(AudioAddresses audioAddresses) {
        this.audioAddresses = audioAddresses;
    }

    public String getUkAudio() {
        return ukAudio;
    }

    public void setUkAudio(String ukAudio) {
        this.ukAudio = ukAudio;
    }

    public Integer getConentId() {
        return conentId;
    }

    public void setConentId(Integer conentId) {
        this.conentId = conentId;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public CnDefinition getCnDefinition() {
        return cnDefinition;
    }

    public void setCnDefinition(CnDefinition cnDefinition) {
        this.cnDefinition = cnDefinition;
    }

    public Integer getNumSense() {
        return numSense;
    }

    public void setNumSense(Integer numSense) {
        this.numSense = numSense;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getSenseId() {
        return senseId;
    }

    public void setSenseId(Integer senseId) {
        this.senseId = senseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Boolean getHasCollinsDefn() {
        return hasCollinsDefn;
    }

    public void setHasCollinsDefn(Boolean hasCollinsDefn) {
        this.hasCollinsDefn = hasCollinsDefn;
    }

    public Boolean getHasOxfordDefn() {
        return hasOxfordDefn;
    }

    public void setHasOxfordDefn(Boolean hasOxfordDefn) {
        this.hasOxfordDefn = hasOxfordDefn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(Boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public EnDefinition getEnDefinition() {
        return enDefinition;
    }

    public void setEnDefinition(EnDefinition enDefinition) {
        this.enDefinition = enDefinition;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getUsAudio() {
        return usAudio;
    }

    public void setUsAudio(String usAudio) {
        this.usAudio = usAudio;
    }

}
